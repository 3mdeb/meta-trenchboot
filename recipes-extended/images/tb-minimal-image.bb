require recipes-core/images/core-image-minimal.bb

IMAGE_FEATURES:append = " ssh-server-openssh"

IMAGE_INSTALL:append = " \
  packagegroup-tb-base \
  packagegroup-tb-utils \
"

IMAGE_FSTYPES += "wic wic.gz wic.bmap"
