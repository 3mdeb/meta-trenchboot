DESCRIPTION = "Safe Boot: Booting Linux Safely"
HOMEPAGE = "https://github.com/osresearch/safeboot"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/LICENSE.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "git://github.com/osresearch/safeboot.git;protocol=https;branch=master;name=safeboot"
SRC_URI += "gitsm://github.com/osresearch/sbsigntools.git;protocol=https;destsuffix=git/sbsigntools;name=sbsigntools"
SRC_URI += "gitsm://git.kernel.org/pub/scm/linux/kernel/git/jejb/efitools.git;destsuffix=git/efitools;protocol=https;name=efitools"
SRC_URI += "gitsm://github.com/tpm2-software/tpm2-tools.git;destsuffix=git/tpm2-tools;protocol=https;name=tpm2-tools"
SRC_URI += "gitsm://github.com/osresearch/tpm2-totp.git;destsuffix=git/tpm2-totp;branch=static-link;protocol=https;name=tpm2-totp"
SRC_URI += "gitsm://github.com/tpm2-software/tpm2-tss.git;destsuffix=git/tpm2-tss;protocol=https;name=tpm2-tss"

SRCREV_FORMAT = "safeboot_sbsigntools_efitools_tpm2-tools_tpm2-totp_tpm2-tss"
SRCREV_safeboot = "0992882728911e81a75605c049de1f1941c49c57"
SRCREV_sbsigntools = "370abb7c49ec2a600f64fcbd441d9297124a5cb7"
SRCREV_efitools = "392836a46ce3c92b55dc88a1aebbcfdfc5dcddce"
SRCREV_tpm2-tools = "c643ff688834d573772c9cc57fcbdf48a7e7735e"
SRCREV_tpm2-totp = "da2d32b076b783adf8ef6fd61b12c1e0de2b698e"
SRCREV_tpm2-tss = "39e4bfc2041f6a7f271710ff33c17ca13c640465"

RDEPENDS_${PN} = "bash"

S = "${WORKDIR}/git"

do_install(){
    install -d ${D}${sysconfdir}/safeboot
    install -d ${D}${sysconfdir}/safeboot/certs
    install -d ${D}${sysconfdir}/initramfs-tools/hooks/
    install -d ${D}${sysconfdir}/initramfs-tools/scripts
    install -d ${D}${sysconfdir}/initramfs-tools/scripts/local-premount/
    install -d ${D}${sysconfdir}/initramfs-tools/scripts/init-top/

    install -m 0755 ${S}/sbin/safeboot ${D}${sbindir}
    install -m 0755 ${S}/sbin/safeboot-tpm-unseal ${D}${bindir}
    install -m 0755 ${S}/sbin/tpm2-attest ${D}${bindir}
    install -m 0755 ${S}/sbin/tpm2-pcr-validate ${D}${bindir}

    install -m 0755 ${S}/sbin/safeboot.conf ${D}${sysconfdir}/safeboot
    install -m 0755 ${S}/sbin/functions.sh ${D}${sysconfdir}/safeboot
    install -m 0755 ${S}/sbin/tpm-certs.txt ${D}${sysconfdir}/safeboot
    install -m 0755 ${S}/sbin/refresh-certs ${D}${sysconfdir}/safeboot

    for file in ${S}/certs/*;do
        install -m 755 $file ${D}${sysconfdir}/safeboot/certs
    done

    install -m 0755 ${S}/bin/sbsign.safeboot ${D}${sbindir}
    install -m 0755 ${S}/bin/sign-efi-sig-list.safeboot ${D}${sbindir}
    install -m 0755 ${S}/bin/tpm2-totp ${D}${sbindir}
    install -m 0755 ${S}/bin/tpm2 ${D}${sbindir}

    install -m 0755 ${S}/initramfs/hooks/dmverity-root ${D}${sysconfdir}/initramfs-tools/hooks/
    install -m 0755 ${S}/initramfs/hooks/safeboot-hooks ${D}${sysconfdir}/initramfs-tools/hooks/
    install -m 0755 ${S}/initramfs/scripts/dmverity-root ${D}${sysconfdir}/initramfs-tools/scripts/local-premount/
    install -m 0755 ${S}/initramfs/scripts/blockdev-readonly ${D}${sysconfdir}/initramfs-tools/scripts/local-premount/
    install -m 0755 ${S}/initramfs/scripts/safeboot-bootmode ${D}${sysconfdir}/initramfs-tools/scripts/init-top/
}

FILES_${PN} += "\
    ${sysconfdir}/safeboot \
    ${sysconfdir}/safeboot/certs \
    ${sysconfdir}/initramfs-tools/hooks/ \
    ${sysconfdir}/initramfs-tools/scripts/local-premount/ \
    ${sysconfdir}/initramfs-tools/scripts/init-top/ \
    ${bindir} \
"
