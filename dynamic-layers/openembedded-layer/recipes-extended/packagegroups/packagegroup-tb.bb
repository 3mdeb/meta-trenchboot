SUMMARY = "Trenchboot support packagegroup"
DESCRIPTION = "Trenchboot support packagegroup"

inherit packagegroup

PACKAGES = " \
    ${PN}-base \
    ${PN}-utils \
"

RDEPENDS_${PN}-utils += "\
    landing-zone \
    safeboot \
    cbmem \
"

RDEPENDS_${PN}-base += "\
    kernel-modules \
    ipxe \
    dhcp-client \
"
