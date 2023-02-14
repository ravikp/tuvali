package io.mosip.tuvali.wallet.transfer.message

import io.mosip.tuvali.transfer.TransferReport

class HandleTransferReportMessage(val report: TransferReport): IMessage(TransferMessageTypes.HANDLE_TRANSFER_REPORT) {}
