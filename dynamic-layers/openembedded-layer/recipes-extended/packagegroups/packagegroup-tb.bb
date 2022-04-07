SUMMARY = "Trenchboot support packagegroup"
DESCRIPTION = "Trenchboot support packagegroup"

inherit packagegroup

PACKAGES = " \
    ${PN}-base \
    ${PN}-core \
    ${PN}-virtualization \
"

RDEPENDS:${PN}-core += "\
    landing-zone \
    safeboot \
    cbmem \
    charra \
"

RDEPENDS:${PN}-base += "\
    kernel-modules \
    vim \
"
