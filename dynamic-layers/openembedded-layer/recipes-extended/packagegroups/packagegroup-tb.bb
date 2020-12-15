SUMMARY = "Trenchboot support packagegroup"
DESCRIPTION = "Trenchboot support packagegroup"

inherit packagegroup

PACKAGES = " \
    ${PN}-base \
    ${PN}-core \
    ${PN}-virtualization \
"

RDEPENDS_${PN}-core += "\
    landing-zone \
    safeboot \
    cbmem \
    charra \
"

RDEPENDS_${PN}-base += "\
    kernel-modules \
    dhcp-client \
    vim \
"
