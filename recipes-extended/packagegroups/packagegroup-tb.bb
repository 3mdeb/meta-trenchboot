SUMMARY = "Trenchboot support packagegroup"
DESCRIPTION = "Trenchboot support packagegroup"

inherit packagegroup

PACKAGES = " \
            ${PN}-base \
            ${PN}-utils \
            ${PN}-tests \
            "

RDEPENDS:${PN}-base += " \
                        kernel-modules \
                        skl \
                        intel-sinit-acm \
                        "

RDEPENDS:${PN}-utils += " \
                         packagegroup-security-tpm2 \
                         util-linux-bash-completion \
                         vim \
                         rsync \
                         kexec \
                         "

RDEPENDS:${PN}-tests = " \
    trenchboot-tests \
"
