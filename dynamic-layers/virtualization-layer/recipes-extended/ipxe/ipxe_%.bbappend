FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://0001-Ignore-specific-warnings.patch"

do_deploy() {
    install -m 0755 ${S}/bin/ipxe.lkrn ${DEPLOY_DIR_IMAGE}
}

addtask do_deploy after do_compile before do_build
