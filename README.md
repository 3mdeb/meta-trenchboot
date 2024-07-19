# meta-trenchboot

[![pipeline status](https://gitlab.com/trenchboot1/3mdeb/meta-trenchboot/badges/master/pipeline.svg)](https://gitlab.com/trenchboot1/3mdeb/meta-trenchboot/-/commits/master)

Meta layer for the Trenchboot purposes

---

## WARNING

This is WIP repo and it is under development. Use it at your own risk.
If you have use-cases for such thing to be developed, please submit
an issue or PR with description of your needs / fixes.

---

## Prerequisites

* Linux PC (tested on `Fedora 39`)

* [docker](https://docs.docker.com/engine/install/fedora/) installed

* [kas-container 3.0.2](https://raw.githubusercontent.com/siemens/kas/3.0.2/kas-container)
  script downloaded and available in [PATH](https://en.wikipedia.org/wiki/PATH_(variable))

    ```bash
    wget -O ~/bin/kas-container https://raw.githubusercontent.com/siemens/kas/3.0.2/kas-container
    chmod +x ~/bin/kas-container
    ```

* `meta-trenchboot` repository cloned

    ```bash
    mkdir yocto
    cd yocto
    git clone https://github.com/3mdeb/meta-trenchboot.git
    ```

* [bmaptool](https://docs.yoctoproject.org/dev-manual/bmaptool.html) installed

    ```bash
    sudo dnf install bmap-tools
    ```

    > You can also use `bmap-tools`
    > [from github](https://github.com/yoctoproject/bmaptool) if it is not
    > available in your distro.

## Build

* From `yocto` directory run:

    ```shell
    kas-container build meta-trenchboot/kas-generic-tb.yml
    ```

* Image build takes time, so be patient and after build's finish you should see
something similar to (the exact tasks numbers may differ):

    ```shell
    Initialising tasks: 100% |###########################################################################################| Time: 0:00:01
    Sstate summary: Wanted 360 Found 0 Missed 8 Current 1810 (0% match, 99% complete)
    NOTE: Executing Tasks
    NOTE: Tasks Summary: Attempted 4774 tasks of which 4749 didn't need to be rerun and all succeeded.
    ```

Thanks to publishing the build cache on cache.dasharo.com the time needed to
finish the process should be significantly decreased.
Using the cache is enabled in `kas/cache.yml` file and can be disabled by removing
reference to this file in `kas/common.yml`:

```yaml
includes:
    - cache.yml
```

This cache can decrease time needed to build image from scratch from hours to
minutes depending on build machine and network connection.

```shell
Sstate summary: Wanted 2170 Local 0 Mirrors 2151 Missed 19 Current 0 (99% match, 0% complete)
NOTE: Executing Tasks
NOTE: Tasks Summary: Attempted 4774 tasks of which 4445 didn't need to be rerun and all succeeded.
```

## Flash

To flash resulting image:

* Find out your device name:

    ```shell
    $ lsblk
    NAME                                     MAJ:MIN RM   SIZE RO TYPE  MOUNTPOINTS
    sdx                                      179:0    0  14.8G  0 disk
    ├─sdx1                                   179:1    0     4M  0 part
    └─sdx2                                   179:2    0     4M  0 part
    ```

    In this case the device name is `/dev/sdx` **but be aware, in next steps
    replace `/dev/sdx` with right device name on your platform or else you can
    damage your system!.**

* From where you ran image build type:

    ```shell
    cd build/tmp/deploy/images/genericx86-64/
    sudo umount /dev/sdx*
    sudo bmaptool copy tb-minimal-image-genericx86-64.rootfs.wic.gz /dev/sdx
    ```

    and you should see output similar to this (the exact size number may differ):

    ```shell
    bmaptool: info: block map format version 2.0
    bmaptool: info: 275200 blocks of size 4096 (1.0 GiB), mapped 73240 blocks (286.1 MiB or 26.6%)
    bmaptool: info: copying image 'tb-minimal-image-genericx86-64.rootfs.wic.gz' to block device '/dev/sdx' using bmap file 'tb-minimal-image-genericx86-64.rootfs.wic.bmap'
    bmaptool: info: 100% copied
    bmaptool: info: synchronizing '/dev/sdx'
    bmaptool: info: copying time: 19.3s, copying speed 14.9 MiB/sec
    ```

## Booting

To run TrenchBoot connect drive with flashed image to target platform and boot
from it. In GRUB menu you can choose normal `boot` or `slaunch-boot`.

```text
                             GNU GRUB  version 2.06

 +----------------------------------------------------------------------------+
 |*boot                                                                       |
 | slaunch-boot                                                               |
 |                                                                            |
 |                                                                            |
 |                                                                            |
 +----------------------------------------------------------------------------+

      Use the ^ and v keys to select which entry is highlighted.
      Press enter to boot the selected OS, `e' to edit the commands
```

After a while you should see login prompt.

```text
early console in extract_kernel
input_data: 0x0000000006801548
input_len: 0x000000000121e953
output: 0x0000000004600000
output_len: 0x00000000033caee8
kernel_total_size: 0x0000000003030000
needed_size: 0x0000000003400000
trampoline_32bit: 0x0000000000000000
Physical KASLR using RDRAND RDTSC...
Virtual KASLR using RDRAND RDTSC...

Decompressing Linux... Parsing ELF... Performing relocations... done.
Booting the kernel (entry_offset: 0x0000000000000000).


Reference Yocto distro for PC Engines hardware 0.2.0 tb ttyS0

tb login:
```

To login use `root` account without password.

## Running in QEMU

It's possible to test image by running it in QEMU. Depending on QEMU
configuration not all features may be available, slaunch boot among others.

To start QEMU:

```shell
cd build/tmp/deploy/images/genericx86-64/
qemu-system-x86_64 -serial stdio -enable-kvm \
    -drive file=tb-minimal-image-genericx86-64.rootfs.wic,if=virtio
```

## Development

### Main components

Below is list of main recipes/components of this layer, path to main recipe file
and short description of component

* tb-minimal-image
    - Recipe: recipes-extended/images/tb-minimal-image.bb
    - Content: Recipe to build image containing all TB components
* intel-sinit-acm
    - Recipe: recipes-support/intel-sinit-acm/intel-sinit-acm_630744.bb
    - Content: Download and deploy Intel ACM `*.bin` files.
* skl
    - Recipe: recipes-support/skl/skl_git.bb
    - Content: Secure Kernel Loader
* linux-tb
    - Recipe: recipes-kernel/linux/linux-tb_6.6.bb
    - Content: Linux kernel
* grub
    - Recipe: recipes-bsp/grub/grub_%.bbappend
* grub-efi
    - Recipe: recipes-bsp/grub/grub-efi_%.bbappend
* grub & grub-efi
    - Recipe: recipes-bsp/grub/grub-tb-common.inc
    - Content: Common config for both recipes

### Source revision

To change branch or commit used by a recipe you have to change `BRANCH` or
`SRCREV` variable in appropriate recipe file. In case of Linux kernel those
variables are named `KBRANCH` and `SRCREV_machine`

### Building modified source

To make development easier you can use `scripts/tb.sh` script.

In order to make and test changes to recipe's source code you first need to
fetch it.

```shell
./scripts/tb.sh modify <recipe>
(...)
INFO: Source tree extracted to /build/workspace/sources/<recipe>
INFO: Using source tree as build directory since that would be the default for this recipe
INFO: Recipe skl now set up to build from /build/workspace/sources/<recipe>
```

All recipes' sources you wish to modify will be in `../build/workspace/sources`.
After modifications, you can try to a build recipe by using
`./scripts/tb.sh build <recipe>` or `./scripts/tb.sh build tb-minimal-image` to
build whole image containing modified recipes.
After building the image, you can [install](#flash) and [boot](#booting) it or
run it in [QEMU](#running-in-qemu).
In case of building individual recipe instead of whole image you have to
[deploy](#deployment) those changes instead of flashing.

### Local files

Files added by Yocto recipe are stored inside `sources/<recipe>/oe-local-files`
folder. Example of local file is `defconfig` file in `linux-tb` recipe

### Linux kernel

To modify Linux config either use `./scripts/tb.sh menuconfig` or modify
`sources/linux-tb/oe-local-files/defconfig`

### Deployment

To deploy component to target machine after making changes you can use:

```shell
./scripts/tb.sh deploy <recipe> <destination>
```

`<destination>` uses the same format as rsync. It should be path to root
directory of TrenchBoot either local or remote.

Examples:

* `./scripts/tb.sh deploy skl /mnt` - TrenchBoot rootfs is mounted at `/mnt` and
boot partition is mounted at `/mnt/boot`
* `./scripts/tb.sh deploy skl root@192.168.4.10:/` - Remote machine running
TrenchBoot.
* `./scripts/tb.sh deploy skl root@192.168.4.10:/mnt` - Remote machine with
TrenchBoot rootfs mounted at `/mnt` and boot partition mounted at `/mnt/boot`

### Finishing

To finish working on source use `./scripts/tb.sh reset <recipe>`. After that
recipe source will be removed.

## Funding

This project is partially funded through
[NGI0 Entrust](https://nlnet.nl/entrust), a fund established by
[NLnet](https://nlnet.nl) with financial support from the European Commission's
[Next Generation Internet](https://ngi.eu) program. Learn more at the
[NLnet project page](https://nlnet.nl/project/TrenchBoot-AMD/).

<p align="center">
    <a href="https://nlnet.nl">
        <img src="https://nlnet.nl/logo/banner.png"
             alt="NLnet foundation logo"
             height="75" />
    </a>
    <a href="https://nlnet.nl/entrust">
        <img src="https://nlnet.nl/image/logos/NGI0_tag.svg"
             alt="NGI Zero logo"
             height="75" />
    </a>
</p>
