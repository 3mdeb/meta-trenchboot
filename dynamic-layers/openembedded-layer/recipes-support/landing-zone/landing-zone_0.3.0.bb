DESCRIPTION = "Landing Zone - DRTM Tool"
HOMEPAGE = "https://github.com/TrenchBoot/landing-zone"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"

SRC_URI = "git://github.com/TrenchBoot/landing-zone.git;protocol=https;branch=master"
SRCREV = "89fc4113166823268b07d27f8b13d82223a2361d"

DEPENDS = "util-linux-native"
RDEPENDS_${PN} = "bash"

S = "${WORKDIR}/git"

EXTRA_OEMAKE += "DEBUG=y"
SECURITY_STACK_PROTECTOR = ""

do_install(){
    install -d ${DEPLOY_DIR_IMAGE}
    install -d ${D}${bindir}/landing-zone

    install -m 0600 ${WORKDIR}/git/lz_header.bin ${DEPLOY_DIR_IMAGE}
    install -m 0600 ${WORKDIR}/git/lz_header.bin ${D}${bindir}/landing-zone/
    install -m 0755 ${WORKDIR}/git/extend_all.sh ${D}${bindir}/landing-zone/
    install -m 0755 ${WORKDIR}/git/util.sh ${D}${bindir}/landing-zone/
}

FILES_${PN} += "${bindir}/landing-zone"
