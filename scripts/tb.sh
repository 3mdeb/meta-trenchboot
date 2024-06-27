#!/bin/bash

set -e

print_help() {
    cat << EOF
Usage:
    ./tb.sh [OPTION]... {modify|reset|menuconfig|build|deploy}
    1.) ./tb.sh modify recipe
    2.) ./tb.sh reset recipe
    3.) ./tb.sh menuconfig
    4.) ./tb.sh build recipe
    5.) ./tb.sh deploy recipe destination

    1.) Fetches source code for the recipe
    2.) Removes recipe source code from disk
    3.) Opens Linux menuconfig
    4.) Builds recipe from fetched source code.
    5.) Copies built recipe to destination

    OPTION:
        -h|--help               - Print this help
        -v|--verbose            - Verbose output

    recipe:
        skl                     - Secure Kernel Loader
        linux-tb                - Linux kernel
        grub                    - GRUB legacy
        grub-efi                - GRUB EFI
        tb-minimal-image        - Complete image with all components. Can only
                                  be build or deployed

    destination:
        Path to target root '/' directory with boot partition mounted at
        '/boot'. Uses same syntax as rsync e.g.:

        /mnt                    - locally mounted TrenchBoot rootfs partition at
                                  '/mnt' with boot partition mounted
                                  at '/mnt/boot'
        root@192.168.4.10:/     - remote machine running TrenchBoot
        root@192.168.4.10:/mnt  - remote machine with TrenchBoot rootfs
                                  partition mounted at '/mnt' and boot partition
                                  mounted at '/mnt/boot'
EOF
}

cleanup() {
    popd &>/dev/null || exit 1
}

usage_error() {
    echo "$1"
    print_help
    exit 1
}

error_msg() {
    echo "$1"
    exit 1
}

parse_args() {
    local positional_args=()
    ACTION=
    RECIPE_ARG=
    DESTINATION_ARG=

    while [ $# -gt 0 ]; do
        case $1 in
            -h|--help)
                print_help
                exit 0
                ;;
            -v|--verbose)
                set -x
                ;;
            -*)
                usage_error "Unknown option"
                ;;
            *)
                positional_args+=("$1")
                ;;
        esac
        shift
    done

    set -- "${positional_args[@]}"

    if [ $# -lt 1 ]; then
        usage_error "Wrong number of positional arguments"
    fi
    ACTION="$1"

    case $ACTION in
        modify|reset|build)
            if [ $# -ne 2 ]; then
                usage_error "Wrong number of arguments"
            fi
            RECIPE_ARG="$2"
            ;;
        deploy)
            if [ $# -ne 3 ]; then
                usage_error "Wrong number of positional arguments"
            fi
            RECIPE_ARG="$2"
            DESTINATION_ARG="$3"
            ;;
        menuconfig)
            RECIPE_ARG="linux-tb"
            ;;
        *)
            usage_error "Wrong positional argument"
            ;;
    esac

    case $RECIPE_ARG in
        skl|grub|grub-efi|linux-tb|tb-minimal-image)
            ;;
        *)
            usage_error "Wrong recipe"
            ;;
    esac
}

menuconfig() {
    if [ -d "build/workspace/sources/$RECIPE_ARG" ]; then
        kas-container shell "$KAS_YAML" \
            -c "devtool menuconfig $RECIPE_ARG"
    else
        error_msg "First use ./tb.sh modify linux-tb"
    fi
}

modify_recipe() {
    if [ ! -d "build/workspace/sources/$RECIPE_ARG" ]; then
        kas-container shell "$KAS_YAML" \
            -c "devtool modify $RECIPE_ARG"
    else
        error_msg "Source is already fetched"
    fi
}

reset_recipe() {
    if [ -d "build/workspace/sources/$RECIPE_ARG" ]; then
        kas-container shell "$KAS_YAML" \
            -c "devtool reset $RECIPE_ARG"
    else
        error_msg "Nothing to reset"
    fi
}

build_recipe() {
    if [ "$RECIPE_ARG" == "tb-minimal-image" ]; then
        rm -rf "build/workspace/sources/grub/gnulib" 2&>/dev/null|| true
        rm -rf "build/workspace/sources/grub-efi/gnulib" 2&>/dev/null || true
        kas-container shell "$KAS_YAML" \
            -c "devtool build-image $RECIPE_ARG"
        return
    fi
    if [ ! -d "build/workspace/sources/$RECIPE_ARG" ]; then
        modify_recipe
    elif [ "$RECIPE_ARG" == "grub" ] || [ "$RECIPE_ARG" == "grub-efi" ]; then
        rm -rf "build/workspace/sources/$RECIPE_ARG/gnulib"
    fi

    kas-container shell "$KAS_YAML" \
        -c "devtool build $RECIPE_ARG"
}

deploy_recipe() {
    local work_dir="build/tmp/work"
    local deploy_dir="build/tmp/deploy/images/genericx86-64"
    local core_path="$work_dir/core2-64-tb-linux"
    local genericx86_path="$work_dir/genericx86_64-tb-linux"
    local kernel_path="$genericx86_path/linux-tb/6.6.1"
    local device_path=
    local tmp_dir=

    case $RECIPE_ARG in
        skl)
            rsync -chavP "$core_path/skl/git/image/" "$DESTINATION_ARG"
            rsync rsync -chrtvP --inplace "$core_path/skl/git/deploy-skl/skl.bin" "$DESTINATION_ARG/boot"
            ;;
        grub)
            error_msg "Not implemented yet"
            ;;
        grub-efi)
            rsync -chavP --exclude "boot" "$core_path/grub-efi/2.06/image/" "$DESTINATION_ARG"
            rsync -chrtvP --inplace "$core_path/grub-efi/2.06/image/boot/" "$DESTINATION_ARG"/boot
            ;;
        linux-tb)
            rsync -chavP --exclude "boot" \
                "$kernel_path/image/" "$DESTINATION_ARG"
            rsync rsync -chrtvP --inplace \
                "$kernel_path/deploy-linux-tb/bzImage-initramfs-genericx86-64.bin" "$DESTINATION_ARG/boot"
            rsync rsync -chrtvP --inplace \
                "$kernel_path/deploy-linux-tb/bzImage-initramfs-genericx86-64.bin" "$DESTINATION_ARG/boot/bzImage"
            ;;
        tb-minimal-image)
            tmp_dir=$(mktemp -d)
            mkdir "$tmp_dir/boot"
            mkdir "$tmp_dir/rootfs"
            device_path=$(sudo losetup --show -Prf "$deploy_dir/tb-minimal-image-genericx86-64.rootfs.wic")
            # shellcheck disable=SC2064
            trap "set +e ; sudo umount ${device_path}p* ; \
                sudo losetup -d $device_path ; set -e ; cleanup" EXIT
            sudo mount -r -o uid=$UID "${device_path}p1" "$tmp_dir/boot"
            sudo mount -r "${device_path}p2" "$tmp_dir/rootfs"
            sudo rsync -chavP --exclude "boot" "$tmp_dir/rootfs/" "$DESTINATION_ARG"
            sudo rsync -chrtvP --inplace "$tmp_dir/boot/" "$DESTINATION_ARG/boot"
            sudo umount "$tmp_dir"/*
            sudo losetup -d "$device_path"
            trap cleanup EXIT
            rmdir "$tmp_dir/boot" "$tmp_dir/rootfs"
            rmdir "$tmp_dir"
            ;;
    esac
}

trap cleanup EXIT

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
LAYER_DIR="$(dirname "$SCRIPT_DIR")"
KAS_YAML="$LAYER_DIR/kas-generic-tb.yml"
pushd "$(dirname "$LAYER_DIR")" &>/dev/null || exit 1
parse_args "$@"

case "$ACTION" in
    modify)
        modify_recipe
        ;;
    reset)
        reset_recipe
        ;;
    menuconfig)
        menuconfig
        ;;
    build)
        build_recipe
        ;;
    deploy)
        deploy_recipe
        ;;
esac
