SUMMARY = "Production SINIT ACM"
DESCRIPTION = "Production release of the SINIT ACM (authenticated code module)"
HOMEPAGE = "https://www.intel.com/content/www/us/en/developer/articles/tool/intel-trusted-execution-technology.html"
LICENSE = "CLOSED"

UNZIPPED_DIR = "630744_003"
SRC_URI = "https://cdrdv2.intel.com/v1/dl/getContent/630744?explicitVersion=true;downloadfilename=${UNZIPPED_DIR}.zip"
SRC_URI[sha256sum] = "0b412c1832bd504d4b8f5fa01b32449c344fe0019e5e4da6bb5d80d393df5e8b"

ALLOW_EMPTY:${PN} = "1"

inherit deploy

do_deploy() {
    install -d ${DEPLOYDIR}/acm
    for file in ${WORKDIR}/${UNZIPPED_DIR}/*.bin
    do
        install -m 0600 ${file} ${DEPLOYDIR}/acm/
    done
}

addtask do_deploy after do_install
