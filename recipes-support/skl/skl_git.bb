DESCRIPTION = "Open source implementation of Secure Loader for AMD Secure Startup."
HOMEPAGE = "https://github.com/TrenchBoot/secure-kernel-loader"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"

SRC_URI = "git://github.com/TrenchBoot/secure-kernel-loader.git;protocol=https;branch=master;name=skl"

SRCREV = "597a840b95cf411d06c2f48cb97c8b679a3ce643"

TUNE_CCARGS:remove = "-msse3 -mfpmath=sse"

DEPENDS = "util-linux-native"
RDEPENDS:${PN} = "bash"

S = "${WORKDIR}/git"

EXTRA_OEMAKE += "DEBUG=y 32=y"
SECURITY_STACK_PROTECTOR = ""

do_install(){
    install -d ${D}${bindir}/skl

    install -m 0600 ${S}/skl.bin ${D}${bindir}/skl/
    install -m 0755 ${S}/extend_all.sh ${D}${bindir}/skl/
    install -m 0755 ${S}/util.sh ${D}${bindir}/skl/
}

inherit deploy

do_deploy() {
    install -d ${DEPLOYDIR}
    install -m 0600 ${S}/skl.bin ${DEPLOYDIR}
}

FILES:${PN} += "${bindir}/skl /boot"

addtask do_deploy after do_compile before do_build
