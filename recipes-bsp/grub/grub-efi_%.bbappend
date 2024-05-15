FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI = " \
    git://github.com/3mdeb/grub.git;branch=${BRANCH};protocol=https \
    file://cfg \
    file://grub.cfg \
"

DEPENDS:append = " grub-tb-native"
DEPENDS:remove = " grub-native"
RDEPENDS:${PN} = "grub-tb-common virtual-grub-bootconf"

BRANCH = "indirect_skl"

S = "${WORKDIR}/git"

GRUB_BUILDIN = "boot linux ext2 fat serial part_msdos part_gpt normal \
                efi_gop iso9660 configfile search loadenv test linux16 \
                slaunch search_label multiboot2"

SRCREV = "e553850a50e468490c633d7a56f99e502fe4f722"

PV = "indirect-skl+${SRCREV}"

do_configure:prepend() {
    cd ${S}
    ${S}/bootstrap
    ${S}/autogen.sh
}

do_deploy:append:class-target() {
    # provide custom grub config
    install -m 644 ${WORKDIR}/grub.cfg ${DEPLOYDIR}
}
