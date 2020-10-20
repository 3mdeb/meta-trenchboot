DESCRIPTION = "Tools for making and managing initramfs images"
LICENSE = "GPLv2"

SRC_URI = "http://http.debian.net/debian/pool/main/i/initramfs-tools/initramfs-tools_0.130.tar.xz"

SRC_URI[md5sum] = "87ad7c876372e8adab0e8c0ffb51fa29"
SRC_URI[sha256sum] = "e9712ccbd8bef51123eda74e4e60435b57b2f3e62bfd02230e0c2dbd84d8e991"

LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

S = "${WORKDIR}/${PN}"

RDEPENDS_${PN} = "libudev cpio util-linux kmod"

do_install () {
        install -d ${D}/etc
        install -d ${D}/etc/bash_completion.d
        install -m 640 bash_completion.d/update-initramfs ${D}/etc/bash_completion.d
        cp -a kernel ${D}/etc
        install -d ${D}/etc/initramfs-tools
        install -D conf/* ${D}/etc/initramfs-tools/

        install -d ${D}/usr/bin
        install -D lsinitramfs ${D}/usr/bin
        install -d ${D}/usr/sbin
        install -D mkinitramfs update-initramfs ${D}/usr/sbin
        install -d ${D}/usr/share/${PN}
        cp -a hooks scripts init ${D}/usr/share/${PN}
        install -D hook-functions ${D}/usr/share/${PN}

        install -d ${D}/usr/share/doc/${PN}
        install -D docs/* ${D}/usr/share/doc/${PN}/
        install -D HACKING ${D}/usr/share/doc/${PN}/

        install -d ${D}/usr/share/man/man5
        install -d ${D}/usr/share/man/man8
        install -D *.5 ${D}/usr/share/man/man5
        install -D *.8 ${D}/usr/share/man/man8
}
