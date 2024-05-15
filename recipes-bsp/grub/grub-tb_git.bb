require recipes-bsp/grub/grub2.inc

SRC_URI = "\
    git://github.com/3mdeb/grub.git;branch=${BRANCH};protocol=https \
    file://0001-add-root-flag-to-grub-bios-setup.patch \
"

BRANCH = "indirect_skl"

S = "${WORKDIR}/git"

SRCREV = "e553850a50e468490c633d7a56f99e502fe4f722"

PV = "${SRCREV}"

RDEPENDS:${PN}-common += "diffutils freetype ${PN}-editenv"
RDEPENDS:${PN}-common += "${PN}-common"
RDEPENDS:${PN}:class-native = ""

RPROVIDES:${PN}-editenv += "${PN}-efi-editenv"

PROVIDES:append:class-native = " grub-efi-tb-native"

PACKAGES =+ "${PN}-editenv ${PN}-common"
FILES:${PN}-editenv = "${bindir}/grub-editenv"
FILES:${PN}-common = " \
    ${bindir} \
    ${sysconfdir} \
    ${sbindir} \
    ${datadir}/grub \
    ${libdir}/grub-tb \
    ${libdir}/grub/i386-pc \
"
ALLOW_EMPTY:${PN} = "1"

do_configure:prepend() {
    cd ${S}
    ${S}/bootstrap
    ${S}/autogen.sh
}

do_install:append () {
    install -d ${D}${sysconfdir}/grub.d
    install -d ${D}${libdir}/grub-tb
    cp -r ${D}${libdir}/grub/i386-pc ${D}${libdir}/grub-tb
    # Remove build host references...
    find "${D}" -name modinfo.sh -type f -exec \
        sed -i \
        -e 's,--sysroot=${STAGING_DIR_TARGET},,g' \
        -e 's|${DEBUG_PREFIX_MAP}||g' \
        -e 's:${RECIPE_SYSROOT_NATIVE}::g' \
        {} +
}

INSANE_SKIP = "arch"
INSANE_SKIP:${PN}-dbg = "arch"

BBCLASSEXTEND = "native nativesdk"
