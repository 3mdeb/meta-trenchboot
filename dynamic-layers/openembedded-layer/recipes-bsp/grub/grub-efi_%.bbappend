BRANCH = "tpm_event_log_support"
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

SRCREV = "4dfd376a2aac1045f467d7e0d70f37b3a6d82eeb"

PV = "2.0.x-tpm+${SRCREV}"

do_configure:prepend() {
(   cd ${S}
    ${S}/bootstrap
    ${S}/autogen.sh )
}

do_deploy:append:class-target() {
    # provide custom grub config
    install -m 644 ${WORKDIR}/grub.cfg ${DEPLOYDIR}
}
