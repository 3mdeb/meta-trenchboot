KBRANCH = "linux-sl-5.5"
SRCREV_machine = "7e95fa7180fec5bd4da5eea1ed20c1d365a0758f"
SRCREV_meta = "2c8ad5a6f5ac9c2cd9f0faa4655531113add4c4f"

require recipes-kernel/linux/linux-yocto.inc

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

SRC_URI = "git://github.com/TrenchBoot/linux.git;protocol=https;branch=${KBRANCH};name=machine; \
           git://git.yoctoproject.org/git/yocto-kernel-cache;protocol=https;type=kmeta;name=meta;branch=yocto-5.4;destsuffix=${KMETA} \
           file://secure-launch.cfg \
	   file://xen.cfg"

SRC_URI_append_pcengines-apux = " file://disable-graphics.cfg \
                                  file://edac.cfg"

KERNEL_FEATURES_append_pcengines-apux = " \
    cfg/smp.scc \
"

KERNEL_FEATURES_remove_pcengines-apux = " \
    cfg/intel.scc \
    features/i915/i915.scc \
    features/usb/touchscreen-composite.scc \
"

LINUX_VERSION ?= "5.5.3"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

DEPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"
KMACHINE_pcengines-apux = "common-pc-64"

PV = "${LINUX_VERSION}+git${SRCPV}"

COMPATIBLE_MACHINE_pcengines-apux = "pcengines-apux"