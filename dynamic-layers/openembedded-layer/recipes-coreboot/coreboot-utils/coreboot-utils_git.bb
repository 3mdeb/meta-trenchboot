DESCRIPTION = "coreboot tools"
SECTION = "coreboot"
HOMEPAGE = "https://github.com/coreboot/coreboot/README.md"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

SRC_URI = " \
    git://github.com/pcengines/coreboot.git;protocol=https;branch=develop \
    file://0001-utils-do-not-override-compiler-variables.patch \
    "

SRCREV = "3830e4478726005a022c708b4b29ae1d280ae480"

PV = "0.1+${SRCREV}"

# this indicates the folder to run do_compile from.
S="${WORKDIR}/git"

PACKAGES =+ "cbmem"

FILES_cbmem = "${bindir}/cbmem"

INSANE_SKIP_cbmem = "ldflags"
TARGET_CFLAGS += "-Wno-error=address-of-packed-member"

# this command will be run to compile your source code. it assumes you are in the
# directory indicated by S. i'm just going to use make and rely on my Makefile
do_compile () {
    oe_runmake -C util/cbmem
}
 
# this will copy the compiled file and place it in ${bindir}, which is /usr/bin
do_install () {
    install -d ${D}${bindir}
    install -m 0755 ${S}/util/cbmem/cbmem ${D}${bindir}
}
