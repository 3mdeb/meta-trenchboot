SRC_URI = "https://github.com/TrenchBoot/xen.git;nobranch=1;tag=v${XEN_REL}"
XEN_REL = "0.4.1"
SRCREV = "abaccc9bf0a7e7eba6228648ced98e8819bd590e"
SRC_URI[sha256sum] = "60f6530ee4297f2a4169b00b259eef97e583356d01428f95d8b382d39edc8cad"

LIC_FILES_CHKSUM ?= "file://COPYING.MIT;md5=838c366f69b72c5df05c96dff79b35f2"

PV = "${XEN_REL}"

S = "${WORKDIR}/git"
