SUMMARY = "Trenchboot support packagegroup"
DESCRIPTION = "Trenchboot support packagegroup"

inherit packagegroup

PACKAGES = " \
            ${PN}-base \
            ${PN}-utils \
            "

RDEPENDS:${PN}-base += " \
                        kernel-modules \
                        skl \
                        "

RDEPENDS:${PN}-utils += " \
                         packagegroup-security-tpm2 \
                         util-linux-bash-completion \
                         vim \
                         "
