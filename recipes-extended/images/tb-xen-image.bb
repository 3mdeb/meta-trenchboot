# nooelint: oelint.file.requirenotfound
require recipes-extended/images/xen-image-minimal.bb

SUMMARY = "A small XEN image just capable of allowing a device to boot."
DESCRIPTION = "A small XEN image just capable of allowing a device to boot."
LICENSE = "MIT"

IMAGE_FEATURES:append = " ssh-server-openssh"

IMAGE_INSTALL:append = " \
                        packagegroup-tb-base \
                        packagegroup-tb-utils \
                        packagegroup-tb-tests \
                        "
