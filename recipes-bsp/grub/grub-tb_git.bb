# nooelint: oelint.file.requirenotfound
require recipes-bsp/grub/grub2.inc

SUMMARY = "GRUB2 is the next-generation GRand Unified Bootloader"

DESCRIPTION = "GRUB2 is the next generation of a GPLed bootloader \
intended to unify bootloading across x86 operating systems. In \
addition to loading the Linux kernel, it implements the Multiboot \
standard, which allows for flexible loading of multiple boot images."

HOMEPAGE = "https://github.com/TrenchBoot/grub"
SECTION = "bootloaders"

LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

PROVIDES:append:class-native = " grub-efi-tb-native"

PV = "${SRCREV}"

SRC_URI = "\
    git://github.com/TrenchBoot/grub.git;branch=${BRANCH};protocol=https \
    file://0001-add-root-flag-to-grub-bios-setup.patch \
"

BRANCH = "intel-txt-aem-2.06-rebased"
SRCREV = "f6dfae51de0fb810bd441889c499db0602934db5"

S = "${WORKDIR}/git"

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

RDEPENDS:${PN}-common += "${PN}-common ${PN}-editenv diffutils freetype"
RDEPENDS:${PN}:class-native = ""
RPROVIDES:${PN}-editenv += "${PN}-efi-editenv"

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
