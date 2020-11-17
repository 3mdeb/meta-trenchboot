KBRANCH = "amd_event_log"
SRCREV_machine = "d7e5f418460fd973dc6b655c00eb94d73da75b95"
SRCREV_meta = "2c8ad5a6f5ac9c2cd9f0faa4655531113add4c4f"

require recipes-kernel/linux/linux-yocto.inc

SRC_URI = "git://github.com/3mdeb/linux.git;protocol=https;branch=${KBRANCH};name=machine; \
           file://defconfig \
           file://debug.cfg \
           file://xen.cfg \
           "

SRC_URI_append_tb-efi = " file://efi.cfg"
SRC_URI_append_pcengines-apux = " file://disable-graphics.cfg \
                                  file://edac.cfg"

LINUX_VERSION ?= "5.5.3"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

DEPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native"

KCONFIG_MODE="--alldefconfig"

PV = "${LINUX_VERSION}+git${SRCPV}"

COMPATIBLE_MACHINE_pcengines-apux = "pcengines-apux"
