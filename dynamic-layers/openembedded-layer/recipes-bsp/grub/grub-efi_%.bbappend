BRANCH = "trenchboot_support"
SRC_URI_remove = " ${GNU_MIRROR}/grub/grub-${PV}.tar.gz"
SRC_URI_append = " git://github.com/3mdeb/grub.git;branch=${BRANCH};protocol=https"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://grub.cfg \
    "

S = "${WORKDIR}/git"

SRCREV = "01c6caa26bb2db0625d04379a34088fa25861a56"

PV = "2.0.4-rc1+${SRCREV}"

do_configure_prepend() {
(   cd ${S}
    ${S}/bootstrap
    ${S}/autogen.sh )
}

do_deploy_append() {
    # provide custom grub config
    rm ${DEPLOYDIR}/grub.cfg
    install -m 644 ${WORKDIR}/grub.cfg ${DEPLOYDIR}
}
