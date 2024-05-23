FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI = " \
           git://github.com/TrenchBoot/grub.git;branch=${BRANCH};protocol=https \
           file://cfg \
           file://grub.cfg \
           "

# nooelint: oelint.append.protvars
PV = "2.06"

# nooelint: oelint.append.protvars
SRCREV = "f6dfae51de0fb810bd441889c499db0602934db5"
S = "${WORKDIR}/git"
BRANCH = "intel-txt-aem-2.06-rebased"

GRUB_BUILDIN = " \
                boot linux ext2 fat serial part_msdos part_gpt normal \
                efi_gop iso9660 configfile search loadenv test linux16 \
                slaunch search_label multiboot2 \
                "

do_configure:prepend() {
    cd ${S}
    ${S}/bootstrap
    ${S}/autogen.sh
}

do_deploy:append:class-target() {
    # provide custom grub config
    install -m 644 ${WORKDIR}/grub.cfg ${DEPLOYDIR}
}
