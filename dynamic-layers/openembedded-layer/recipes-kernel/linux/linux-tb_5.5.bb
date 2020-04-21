KBRANCH = "linux-sl-5.5"
SRCREV_machine = "eed5cdf480ee3761d18294d64ac7e2184229b51c"
SRCREV_meta = "2c8ad5a6f5ac9c2cd9f0faa4655531113add4c4f"

require recipes-kernel/linux/linux-yocto.inc

SRC_URI = "git://github.com/TrenchBoot/linux.git;protocol=https;branch=${KBRANCH};name=machine; \
           file://tpm2.cfg \
           file://cgroups.cfg \
           file://net.cfg"

SRC_URI_append_tb-xen = " file://xen.cfg"
SRC_URI_append_pcengines-apux = " file://disable-graphics.cfg \
                                  file://edac.cfg"

LINUX_VERSION ?= "5.5.3"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

DEPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native"

KCONFIG_MODE="--alldefconfig"
KBUILD_DEFCONFIG = "securelaunch_defconfig"

PV = "${LINUX_VERSION}+git${SRCPV}"

COMPATIBLE_MACHINE_pcengines-apux = "pcengines-apux"
