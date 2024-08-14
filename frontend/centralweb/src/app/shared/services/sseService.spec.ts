import { TestBed } from '@angular/core/testing';
import { SseService } from './sseService';

describe('SseService', () => {
  let service: SseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SseService]
    });
    service = TestBed.inject(SseService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should create an observable for ticket updates', () => {
    const fakeEvent = new MessageEvent('message', { data: JSON.stringify({ update: 'new update' }) });

    spyOn(window, 'EventSource').and.returnValue({
      addEventListener: (type: string, listener: EventListener) => {
        if (type === 'message') {
          setTimeout(() => listener(fakeEvent), 0);
        }
      },
      close: jasmine.createSpy('close')
    } as any);

    service.getTicketUpdates().subscribe(data => {
      expect(data).toEqual({ update: 'new update' });
    });
  });

});
