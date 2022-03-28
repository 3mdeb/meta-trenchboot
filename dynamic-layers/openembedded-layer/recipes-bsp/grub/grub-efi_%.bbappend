BRANCH = "TB_SKINIT_upstream"
SRC_URI:remove = " ${GNU_MIRROR}/grub/grub-${PV}.tar.gz"
SRC_URI:append = " git://github.com/3mdeb/grub.git;branch=${BRANCH};protocol=https"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://grub.cfg \
    file://autogen.sh-exclude-pc-fixed.patch \
    "

SRC_URI:remove = "file://autogen.sh-exclude-pc.patch"

S = "${WORKDIR}/git"

GRUB_BUILDIN = "boot linux ext2 fat serial part_msdos part_gpt normal \
                efi_gop iso9660 configfile search loadenv test linux16 \
                slaunch search_label multiboot2"

SRCREV = "1a0cbebe77383036bd7cfcaec4d56b0c3a8fa912"

PV = "2.0.4-rc1+${SRCREV}"

do_configure:prepend() {
(   cd ${S}
    ${S}/bootstrap
    ${S}/autogen.sh )
}

do_deploy:append:class-target() {
    # provide custom grub config
    install -m 644 ${WORKDIR}/grub.cfg ${DEPLOYDIR}
}
