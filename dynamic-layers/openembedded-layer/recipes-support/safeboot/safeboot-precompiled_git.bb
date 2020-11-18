DESCRIPTION = "Safe Boot: Booting Linux Safely"
HOMEPAGE = "https://github.com/osresearch/safeboot"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/LICENSE.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"


SRC_URI = "git://github.com/osresearch/safeboot.git;protocol=https;branch=master;name=safeboot"
SRC_URI += "https://cloud.3mdeb.com/index.php/s/MJJNgADr6wxm3mN/download;protocol=https;downloadfilename=safeboot.tar.xz"

SRCREV_safeboot = "a1f020c5aac3eb86be02f4dd5aa590c65acf1f4d"
SRC_URI[md5sum] = "850a5712c64f667bf32f8335a5c2c31c"
SRC_URI[sha256sum] = "489a2e5b09bf4dcad1260a15661af1a078b86ed5ac4399283bf5db762b1b8a78"


RDEPENDS_${PN} = "bash"

S = "${WORKDIR}/git"

do_compile[noexec] = "1"

do_install(){
    install -d ${D}${sbindir}
    install -d ${D}${sysconfdir}/safeboot
    install -d ${D}${sysconfdir}/safeboot/certs
    install -d ${D}${sysconfdir}/initramfs-tools/hooks/
    install -d ${D}${sysconfdir}/initramfs-tools/scripts
    install -d ${D}${sysconfdir}/initramfs-tools/scripts/local-premount/
    install -d ${D}${sysconfdir}/initramfs-tools/scripts/init-top/

    install -m 0755 ${S}/sbin/safeboot ${D}${sbindir}
    install -m 0755 ${S}/sbin/safeboot-tpm-unseal ${D}${sbindir}
    install -m 0755 ${S}/sbin/tpm2-attest ${D}${sbindir}
    install -m 0755 ${S}/sbin/tpm2-pcr-validate ${D}${sbindir}

    install -m 0755 ${S}/safeboot.conf ${D}${sysconfdir}/safeboot
    install -m 0755 ${S}/functions.sh ${D}${sysconfdir}/safeboot
    install -m 0755 ${S}/tpm-certs.txt ${D}${sysconfdir}/safeboot
    install -m 0755 ${S}/refresh-certs ${D}${sysconfdir}/safeboot

    for file in ${S}/certs/*;do
        install -m 755 $file ${D}${sysconfdir}/safeboot/certs
    done

    install -m 0755 ${WORKDIR}${sbindir}/sbsign.safeboot ${D}${sbindir}
    install -m 0755 ${WORKDIR}${sbindir}/sign-efi-sig-list.safeboot ${D}${sbindir}

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
"

DEPENDS += "gnu-efi openssl"
RDEPENDS_${PN} += "tpm2-totp tpm2-tools"

INSANE_SKIP_${PN} += "already-stripped"
