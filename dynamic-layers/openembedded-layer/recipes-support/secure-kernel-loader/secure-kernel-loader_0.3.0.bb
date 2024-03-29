DESCRIPTION = "Secure Kernel Loader"
HOMEPAGE = "https://github.com/TrenchBoot/secure-kernel-loader"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"

SRC_URI = "git://github.com/TrenchBoot/secure-kernel-loader.git;protocol=https;branch=master;name=secure-kernel-loader"
SRC_URI += "gitsm://github.com/TrenchBoot/tpmlib.git;protocol=https;destsuffix=git/tpmlib;name=tpmlib"

SRCREV_secure-kernel-loader = "3432f4398652727f402b710c2fea4e3f1efecce6"
SRCREV_tpmlib = "3e41e94fd4c17fe0d73d556501efa572346fb75a"

TUNE_CCARGS:remove = "-msse3 -mfpmath=sse"

DEPENDS = "util-linux-native"
RDEPENDS:${PN} = "bash"

S = "${WORKDIR}/git"

EXTRA_OEMAKE += "DEBUG=y 32=y"
SECURITY_STACK_PROTECTOR = ""

do_install(){
    install -d ${D}${bindir}/secure-kernel-loader

    install -m 0600 ${S}/skl.bin ${D}${bindir}/secure-kernel-loader/
    install -m 0755 ${S}/extend_all.sh ${D}${bindir}/secure-kernel-loader/
    install -m 0755 ${S}/util.sh ${D}${bindir}/secure-kernel-loader/
}

inherit deploy

do_deploy() {
    install -d ${DEPLOYDIR}
    install -m 0600 ${S}/skl.bin ${DEPLOYDIR}
}

FILES:${PN} += "${bindir}/secure-kernel-loader /boot"

addtask do_deploy after do_compile before do_build
