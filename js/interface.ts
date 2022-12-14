import type { EmitterSubscription } from 'react-native';

export interface IdpassSmartshare {
  /**
   * Do not invoke. Only used to trigger autolink in iOS builds.
   */
  noop: () => void;
  getConnectionParameters: () => string;
  setConnectionParameters: (params: string) => void;
  getConnectionParametersDebug: () => string;
  createConnection: (mode: ConnectionMode, callback: () => void) => void;
  destroyConnection: () => void;
  send: (message: string, callback: () => void) => void;
  handleNearbyEvents: (
    callback: (event: NearbyEvent) => void
  ) => EmitterSubscription;
  handleLogEvents: (
    callback: (event: NearbyLog) => void
  ) => EmitterSubscription;
}

export declare type ConnectionMode = 'advertiser' | 'discoverer';
export declare type TransferUpdateStatus =
  | 'SUCCESS'
  | 'FAILURE'
  | 'IN_PROGRESS'
  | 'CANCELED';
export declare type NearbyEvent =
  | {
      type: 'msg';
      data: string;
    }
  | {
      type: 'transferupdate';
      data: TransferUpdateStatus;
    }
  | {
      type: 'onDisconnected';
      data: string;
    };
export interface NearbyLog {
  log: string;
}
export interface ConnectionParams {
  cid: string;
  pk: string;
}
