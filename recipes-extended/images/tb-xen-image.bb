require recipes-extended/images/tb-xen-minimal-image.bb

DISTRO_FEATURES += "\
    xen \
"

#IMAGE_INSTALL += " \
#    xen-misc \
#"
