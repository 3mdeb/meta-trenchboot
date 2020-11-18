require recipes-bsp/grub/grub2.inc

BRANCH = "TB_SKINIT_upstream"
SRC_URI_remove = " ${GNU_MIRROR}/grub/grub-${PV}.tar.gz"
SRC_URI_append = " git://github.com/3mdeb/grub.git;branch=${BRANCH};protocol=https"
SRC_URI_append = " file://0001-add-root-flag-to-grub-bios-setup.patch"

SRC_URI_remove = "file://CVE-2020-10713.patch"
SRC_URI_remove = "file://calloc-Make-sure-we-always-have-an-overflow-checking.patch"
SRC_URI_remove = "file://lvm-Add-LVM-cache-logical-volume-handling.patch"
SRC_URI_remove = "file://CVE-2020-14308-calloc-Use-calloc-at-most-places.patch"
SRC_URI_remove = "file://CVE-2020-14309-CVE-2020-14310-CVE-2020-14311-malloc-Use-overflow-checking-primitives-where-we-do-.patch"
SRC_URI_remove = "file://CVE-2020-15706-script-Avoid-a-use-after-free-when-redefining-a-func.patch"
SRC_URI_remove = "file://CVE-2020-15707-linux-Fix-integer-overflows-in-initrd-size-handling.patch"
SRC_URI_remove = "file://safemath-Add-some-arithmetic-primitives-that-check-f.patch"
SRC_URI_remove = "file://script-Remove-unused-fields-from-grub_script_functio.patch"

S = "${WORKDIR}/git"

SRCREV = "1a0cbebe77383036bd7cfcaec4d56b0c3a8fa912"

PV = "2.0.4-rc1+${SRCREV}"

RDEPENDS_${PN}-common += "${PN}-editenv"
RDEPENDS_${PN} += "diffutils freetype ${PN}-common"

RPROVIDES_${PN}-editenv += "${PN}-efi-editenv"

PACKAGES =+ "${PN}-editenv ${PN}-common"
FILES_${PN}-editenv = "${bindir}/grub-editenv"
FILES_${PN}-common = " \
    ${bindir} \
    ${sysconfdir} \
    ${sbindir} \
    ${datadir}/grub \
    ${libdir}/grub-tb \
    ${libdir}/grub/i386-pc \
"

FILES_${PN}-common_append_aarch64 = " \
    ${libdir}${BPN} \
"

# grub is blackisted in
# meta/conf/distro/include/security_flags.inc
# it won't compile with security flags enabled
SECURITY_CFLAGS = ""

do_configure_prepend() {
(   cd ${S}
    ${S}/bootstrap
    ${S}/autogen.sh )
}

do_install_append () {
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
