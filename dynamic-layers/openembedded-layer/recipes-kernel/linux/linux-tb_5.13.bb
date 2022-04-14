KBRANCH = "linux-sl-5.13-amd"
KMETA = "kernel-meta"
SRCREV_machine = "7fe9bb33721975fc796e4114b7370bed9afefffe"
SRCREV_meta = "f9e349e174542980f72dcd087445d0106b7a5e75"

require recipes-kernel/linux/linux-yocto.inc

SRC_URI = "git://github.com/TrenchBoot/linux.git;protocol=https;branch=${KBRANCH};name=machine; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.14;destsuffix=${KMETA} \
           file://defconfig \
           file://debug.cfg \
           file://xen.cfg \
           "

SRC_URI:append:tb-efi = " file://efi.cfg"
SRC_URI:append:pcengines-apux = " file://disable-graphics.cfg \
                                  file://edac.cfg"

LINUX_VERSION ?= "5.13.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

DEPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native"

KCONFIG_MODE="--alldefconfig"

PV = "${LINUX_VERSION}+git${SRCPV}"

COMPATIBLE_MACHINE_pcengines-apux = "pcengines-apux"
