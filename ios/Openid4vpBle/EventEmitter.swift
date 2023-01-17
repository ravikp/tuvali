import Foundation

@available(iOS 13.0, *)
class EventEmitter {
    
    public static var sharedInstance = EventEmitter()
    
    static var eventEmitter: Openid4vpBle!
    
    private  init() {}
    
    func registerEventEmitter(eventEmitter: Openid4vpBle) {
        EventEmitter.eventEmitter = eventEmitter
    }
    
    func dispatch(name: String, body: Any?) {
        EventEmitter.eventEmitter.sendEvent(withName: name, body: body)
    }
    
    func emitNearbyEvent(event: String) {
        var eventData : [String: String] = [:]
        eventData["type"] = event
        dispatch(name: "EVENT_NEARBY", body: eventData)
    }
    
    func emitNearbyMessage(event: String, data: String) {
        var eventData: [String: String] = [:]
        eventData["data"] = String(format: "%s\n%s", event, data)
        eventData["type"] = "msg"
        
        dispatch(name: "EVENT_NEARBY", body: eventData)
    }
    
    lazy var allEvents: [String] = {
        return [
            "EVENT_NEARBY",
            "EVENT_LOG",
            "exchange-receiver-info",
        ]
    }()
}

