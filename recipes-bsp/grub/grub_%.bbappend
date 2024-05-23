FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI = " \
    git://github.com/TrenchBoot/grub.git;branch=${BRANCH};protocol=https \
    file://0001-add-root-flag-to-grub-bios-setup.patch \
"

# nooelint: oelint.append.protvars
PV = "2.06"

BRANCH = "intel-txt-aem-2.06-rebased"
# nooelint: oelint.append.protvars
SRCREV = "f6dfae51de0fb810bd441889c499db0602934db5"

S = "${WORKDIR}/git"

FILES:${PN}-common += " \
    ${libdir}/grub-tb \
    ${libdir}/grub/i386-pc \
"

RDEPENDS:${PN}-common += "diffutils freetype"

do_configure:prepend() {
    cd ${S}
    ${S}/bootstrap
    ${S}/autogen.sh
}

do_install:append () {
    install -d ${D}${libdir}/grub-tb
    cp -r ${D}${libdir}/grub/i386-pc ${D}${libdir}/grub-tb
}

INSANE_SKIP:${PN}-common += "arch"
