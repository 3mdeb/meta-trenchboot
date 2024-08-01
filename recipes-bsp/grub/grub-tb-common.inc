SRC_URI = "git://github.com/TrenchBoot/grub.git;name=grub;branch=${BRANCH_grub};protocol=https \
           git://github.com/coreutils/gnulib;name=gnulib;branch=${BRANCH_gnulib};protocol=https;destsuffix=git/gnulib"

PV = "2.06"

BRANCH_grub = "intel-txt-aem-2.06-rebased"
SRCREV_grub = "85ccfdc700a1640c148909cbbad777ba6d7d124b"

# We add gnulib to SRC_URI to avoid downloading it during the do_configure().
# The bootstrap script seems to be smart enough to skip fetching repo again
# if the local revision is correct. Another way to "solve" that would be
# to add gnulib as a submodule in our fork.
#
# We need to update this with each grub rebase, to make sure it is in sync
# with the bootstrap.conf, otherwise it will be sync during do_configure()
# again.
BRANCH_gnulib = "master"
SRCREV_gnulib = "9f48fb992a3d7e96610c4ce8be969cff2d61a01b"

SRCREV_FORMAT = "grub_gnulib"

S = "${WORKDIR}/git"

do_configure:prepend() {
    cd ${S}
    ${S}/bootstrap
    ${S}/autogen.sh
}
