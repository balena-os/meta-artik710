#!/bin/sh

function gen_bd_addr {
	[ -d /opt/.bd_addr ] || rm -f /opt/.bd_addr

	macaddr=$(dd if=/dev/urandom bs=1024 count=1 2>/dev/null|md5sum|sed 's/^\(..\)\(..\)\(..\)\(..\)\(..\).*$/00:\1:\2:\3:\4:\5/')
	echo $macaddr > /opt/.bd_addr
	chmod 400 /opt/.bd_addr
	sync
}

for x in $(cat /proc/cmdline); do
	case $x in
	bd_addr=*)
		BD_ADDR=${x#bd_addr=}
		;;
	esac
done

if [ "$BD_ADDR" == "" ]; then
	if [ ! -f "/opt/.bd_addr" ]; then
		gen_bd_addr ${ARTIK_DEV}
	fi

	BD_ADDR=`cat /opt/.bd_addr`
	if [ "$BD_ADDR" == "" ]; then
		gen_bd_addr ${ARTIK_DEV}
		BD_ADDR=`cat /opt/.bd_addr`
	fi
fi

pushd `dirname $0`

# BT use uar1 on ARTIK7.
TTY_NUM=1
./brcm_patchram_plus --patchram BCM4345C0_003.001.025.0108.0000_Generic_UART_37_4MHz_wlbga_ref_iLNA_iTR_eLG_BT40_test_only.hcd \
	--no2bytes --baudrate 3000000 \
	--use_baudrate_for_download /dev/ttySAC${TTY_NUM} \
	--bd_addr ${BD_ADDR} \
	--enable_hci > /dev/null &

echo $! > /run/brcm_patchram_plus.pid
