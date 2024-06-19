require grub-tb-common.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://0001-add-root-flag-to-grub-bios-setup.patch"

FILES:${PN}-common += " \
    ${libdir}/grub/i386-pc \
"

RDEPENDS:${PN}-common += "diffutils freetype"

INSANE_SKIP:${PN}-common += "arch"
