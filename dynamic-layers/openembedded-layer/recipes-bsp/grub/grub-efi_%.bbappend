BRANCH = "TB_SKINIT_upstream"
SRC_URI_remove = " ${GNU_MIRROR}/grub/grub-${PV}.tar.gz"
SRC_URI_append = " git://github.com/3mdeb/grub.git;branch=${BRANCH};protocol=https"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://grub.cfg \
    file://autogen.sh-exclude-pc-fixed.patch \
    "

SRC_URI_remove = "file://autogen.sh-exclude-pc.patch"

S = "${WORKDIR}/git"

GRUB_BUILDIN = "boot linux ext2 fat serial part_msdos part_gpt normal \
                efi_gop iso9660 configfile search loadenv test linux16 \
                slaunch search_label multiboot2"

SRCREV = "1a0cbebe77383036bd7cfcaec4d56b0c3a8fa912"

PV = "2.0.4-rc1+${SRCREV}"

do_configure_prepend() {
(   cd ${S}
    ${S}/bootstrap
    ${S}/autogen.sh )
}

do_deploy_append_class-target() {
    # provide custom grub config
    install -m 644 ${WORKDIR}/grub.cfg ${DEPLOYDIR}
}
