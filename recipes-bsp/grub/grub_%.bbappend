require grub-tb-common.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://0001-add-root-flag-to-grub-bios-setup.patch"

FILES:${PN}-common += " \
    ${libdir}/grub-tb \
    ${libdir}/grub/i386-pc \
"

RDEPENDS:${PN}-common += "diffutils freetype"

do_install:append () {
    install -d ${D}${libdir}/grub-tb
    cp -r ${D}${libdir}/grub/i386-pc ${D}${libdir}/grub-tb
}

INSANE_SKIP:${PN}-common += "arch"
