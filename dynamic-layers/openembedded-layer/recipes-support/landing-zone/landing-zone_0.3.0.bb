DESCRIPTION = "Landing Zone - DRTM Tool"
HOMEPAGE = "https://github.com/TrenchBoot/landing-zone"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"

SRC_URI = "git://github.com/TrenchBoot/landing-zone.git;protocol=https;branch=master"
SRCREV = "2750c7b50d9d845887fad2bce3afe61f25da8505"


DEPENDS = "util-linux-native"
RDEPENDS_${PN} = "bash"

S = "${WORKDIR}/git"

EXTRA_OEMAKE += "DEBUG=y"
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

FILES_${PN} += "${bindir}/landing-zone"

addtask do_deploy after do_compile before do_build
