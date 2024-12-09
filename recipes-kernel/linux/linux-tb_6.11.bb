# nooelint: oelint.file.requirenotfound
require recipes-kernel/linux/linux-yocto.inc

SUMMARY = "Linux kernel"
DESCRIPTION = "Linux kernel 6.11"
HOMEPAGE = "https://github.com/TrenchBoot/linux"
SECTION = "kernel"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

DEPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native coreutils-native"

PV = "6.11.0-rc7"
KBRANCH = "linux-sl-master-9-12-24-v11-amd-no-psp"
KMETA = "kernel-meta"
SRC_URI = "\
    git://github.com/TrenchBoot/linux.git;protocol=https;branch=${KBRANCH};name=machine; \
    git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;protocol=https;branch=yocto-6.6;destsuffix=${KMETA} \
    file://defconfig \
    file://debug.cfg \
    file://efi.cfg \
"
SRCREV_machine = "63ed6bb791786eab4b96d8510cbd23d4d87d356e"
SRCREV_meta = "49698cadd79745fa26aa7ef507c16902250c1750"

LINUX_VERSION ?= "6.11-rc7"

KCONFIG_MODE = "--alldefconfig"

COMPATIBLE_MACHINE:pcengines-apux = "pcengines-apux"
