EXTRA_OECONF += "--with-udevrulesdir=/lib/udev/rules.d/"

FILES_${PN} = "\
    /lib/udev \
"

do_deploy_append() {
    install -m 0755 ${S}${sbindir}/tpm2-totp ${D}${sbindir}
}
