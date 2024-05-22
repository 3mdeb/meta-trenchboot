KBRANCH = "linux-sl-6.7"
KMETA = "kernel-meta"
SRCREV_machine = "f34b37412e98000ef2c1a067af2c90ac18a99352"
SRCREV_meta = "49698cadd79745fa26aa7ef507c16902250c1750"

require recipes-kernel/linux/linux-yocto.inc

SRC_URI = "git://github.com/TrenchBoot/linux.git;protocol=https;branch=${KBRANCH};name=machine; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-6.6;destsuffix=${KMETA} \
           file://defconfig \
           file://debug.cfg \
           file://efi.cfg \
           file://tpm2.cfg \
           "

LINUX_VERSION ?= "6.7.8"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

DEPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native"

KCONFIG_MODE="--alldefconfig"

PV = "${LINUX_VERSION}"

COMPATIBLE_MACHINE_pcengines-apux = "pcengines-apux"
