SRC_URI = "git://github.com/TrenchBoot/grub.git;branch=${BRANCH};protocol=https"

PV = "2.06"

BRANCH = "intel-txt-aem-2.06-rebased"
SRCREV = "85ccfdc700a1640c148909cbbad777ba6d7d124b"

S = "${WORKDIR}/git"

do_configure:prepend() {
    cd ${S}
    ${S}/bootstrap
    ${S}/autogen.sh
}
