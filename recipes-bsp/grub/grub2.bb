SUMMARY = "GRUB2 is the next-generation GRand Unified Bootloader"

DESCRIPTION = "GRUB2 is the next generaion of a GPLed bootloader \
intended to unify bootloading across x86 operating systems. In \
addition to loading the Linux kernel, it implements the Multiboot \
standard, which allows for flexible loading of multiple boot images."

HOMEPAGE = "http://www.gnu.org/software/grub/"
SECTION = "bootloaders"

LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

SRC_URI = "https://github.com/3mdeb/grub2/tree/3425b85e83dbea3dffa0f88d4c2051c771ebc033/;protocol=file"

# Na podstawie DEPENDS
DEPENDS = "autoconf-native automake-native bison-native flex-native gettext-native gnu-config-native grub-efi-native libtool-cross libtool-native texinfo-native"
