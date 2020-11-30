SUMMARY = "Trenchboot support packagegroup"
DESCRIPTION = "Trenchboot support packagegroup"

inherit packagegroup

PACKAGES = " \
    ${PN}-base \
    ${PN}-core \
"

RDEPENDS_${PN}-core += "\
    landing-zone \
    safeboot \
    cbmem \
"

RDEPENDS_${PN}-base += "\
    kernel-modules \
    ipxe \
    dhcp-client \
"
