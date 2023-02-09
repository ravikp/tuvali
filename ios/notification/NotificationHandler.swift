//
//  NotificationHandler.swift
//  Openid4vpBle
//
//  Created by krishnakumart on 08/02/23.
//  Copyright © 2023 Facebook. All rights reserved.
//

import Foundation

// Notification handler is created on connection
// One notfication handler object to hold all tokens
// Deregister observers using tokens at the end of VC transfer

final class CustomNotificationToken: NSObject {
    let notificationCenter: NotificationCenter
    let token: Any
    init(notificationCenter: NotificationCenter = .default, token: Any) {
        self.notificationCenter = notificationCenter
        self.token = token
    }
    deinit {
        print("Removing notification observer for \(token)")
        notificationCenter.removeObserver(token)
    }
}

extension NotificationCenter {
    func observe(name: NSNotification.Name?, object obj: Any?,
                 queue: OperationQueue?, using block: @escaping (Notification) -> ())
    -> CustomNotificationToken
    {
        let token = addObserver(forName: name, object: obj, queue: queue, using: block)
        print("creating custom notification for name: \(String(describing: name))")
        return CustomNotificationToken(notificationCenter: self, token: token)
    }
}

final class NotificationHandler  {
    var observerRegistrationTokens: NSMutableArray? = []
    
    func registerCallbackForEvent(event: NotificationEvent, callback: @escaping (_ notification: Notification) -> Void) {
        let token: AnyObject = NotificationCenter.default.observe(name: Notification.Name(rawValue: event.rawValue), object: nil, queue: nil) { [unowned self] notification in
            print("Handling registered notification for \(notification.name.rawValue)")
            callback(notification)
        }
        observerRegistrationTokens?.add(token)
        print("registered observer tokens \(String(describing: observerRegistrationTokens))")
    }
    
    func getObjectFromNotification(notification: Notification, userInfoKey: String) -> Any? {
        return notification.userInfo![userInfoKey]
    }

    deinit {
        print("Removing notification observers")
        observerRegistrationTokens = nil
    }
}
