require recipes-bsp/grub/grub2.inc

BRANCH = "indirect_skl"
SRC_URI:remove = " ${GNU_MIRROR}/grub/grub-${PV}.tar.gz"
SRC_URI:remove = " file://0001-RISC-V-Restore-the-typcast-to-long.patch"

SRC_URI = "\
            git://github.com/3mdeb/grub.git;branch=${BRANCH};protocol=https \
            file://0001-add-root-flag-to-grub-bios-setup.patch \
"
#file://rootflag.patch

#to work with grub tpm support 3mdeb branch:
#file://0001-gentpl.py-Decompressor-bugfix.patch
#file://0001-add-root-flag-to-grub-bios-setup.patch
#file://0001-autogen.sh-exclude-pc.patch
#file://0001-Restore-umask-for-grub.cfg.patch

S = "${WORKDIR}/git"

SRCREV = "b7c2702bd3b2d48827640711022aaabc088e5a49"

PV = "indirect-skl+${SRCREV}"

RDEPENDS:${PN}-common += "${PN}-editenv"
RDEPENDS:${PN} += "diffutils freetype ${PN}-common"

RPROVIDES:${PN}-editenv += "${PN}-efi-editenv"

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

FILES:${PN}-common:append:aarch64 = " \
    ${libdir}${BPN} \
"

# grub is blackisted in
# meta/conf/distro/include/security_flags.inc
# it won't compile with security flags enabled
SECURITY_CFLAGS = ""

do_configure:prepend() {
(   cd ${S}
    ${S}/bootstrap
    ${S}/autogen.sh )
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
