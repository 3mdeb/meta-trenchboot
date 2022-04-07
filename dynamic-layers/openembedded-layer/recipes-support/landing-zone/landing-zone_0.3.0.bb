DESCRIPTION = "Landing Zone - DRTM Tool"
HOMEPAGE = "https://github.com/TrenchBoot/landing-zone"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"

SRC_URI = "git://github.com/3mdeb/landing-zone.git;protocol=https;branch=headers_redesign;name=landing-zone"
SRC_URI += "gitsm://github.com/TrenchBoot/tpmlib.git;protocol=https;destsuffix=git/tpmlib;name=tpmlib"

SRCREV_landing-zone = "c9f3d0c892c2553c3a354706fe21e07c8d2321b5"
SRCREV_tpmlib = "3e41e94fd4c17fe0d73d556501efa572346fb75a"

TUNE_CCARGS:remove = "-msse3 -mfpmath=sse"

DEPENDS = "util-linux-native"
RDEPENDS:${PN} = "bash"

S = "${WORKDIR}/git"

EXTRA_OEMAKE += "DEBUG=y 32=y"
SECURITY_STACK_PROTECTOR = ""

do_install(){
    install -d ${D}${bindir}/landing-zone

    install -m 0600 ${S}/lz_header.bin ${D}${bindir}/landing-zone/
    install -m 0755 ${S}/extend_all.sh ${D}${bindir}/landing-zone/
    install -m 0755 ${S}/util.sh ${D}${bindir}/landing-zone/
}

inherit deploy

do_deploy() {
    install -d ${DEPLOYDIR}
    install -m 0600 ${S}/lz_header.bin ${DEPLOYDIR}
}

FILES:${PN} += "${bindir}/landing-zone /boot"

addtask do_deploy after do_compile before do_build
