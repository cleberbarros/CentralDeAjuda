import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TicketService } from './ticket.service';
import { environment } from '../../../../environment';

describe('TicketService', () => {
  let service: TicketService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [TicketService]
    });
    service = TestBed.inject(TicketService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('getTicketsByUserIdAndPage should return expected tickets', () => {
    const mockResponse = {
      content: [
        {
          id: 1,
          userId: 1,
          subject: 'Subject Test',
          description: 'Description Test',
          status: 'OPEN',
          createdAt: '2023-03-18T12:00:00',
          closedAt: null
        }
      ],
      pageable: {
        pageNumber: 0,
        pageSize: 10,
        sort: {
          empty: false,
          sorted: true,
          unsorted: false
        },
        offset: 0,
        unpaged: false,
        paged: true
      },
      totalPages: 1,
      totalElements: 1,
      last: true,
      first: true,
      size: 10,
      number: 0,
      sort: {
        empty: false,
        sorted: true,
        unsorted: false
      },
      numberOfElements: 1,
      empty: false
    };

    const userId = 1;
    const filter = '';
    const page = 0;
    const size = 10;

    service.getTicketsByUserIdAndPage(userId, filter, page, size).subscribe(response => {
      expect(response).toEqual(mockResponse);
    });

    const req = httpTestingController.expectOne(`${environment.apiUrl}/tickets/user/${userId}?page=${page}&size=${size}&sort=id`);
    expect(req.request.method).toBe('GET');
    req.flush(mockResponse);
  });

  it('createTicket should post and return the ticket', () => {
    const ticketDTO = {
      userId: 1,
      subject: 'New Subject',
      description: 'New Description'
    };

    const mockTicket = {
      id: 10,
      userId: 1,
      subject: 'New Subject',
      description: 'New Description',
      status: 'OPEN',
      createdAt: '2023-03-20T12:00:00',
      closedAt: null
    };

    service.createTicket(ticketDTO).subscribe(ticket => {
      expect(ticket).toEqual(mockTicket);
    });

    const req = httpTestingController.expectOne(`${environment.apiUrl}/tickets`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(ticketDTO);
    req.flush(mockTicket);
  });

  it('closeTicket should patch the ticket and return updated data', () => {
    const ticketId = 10;
    const mockPatchResponse = { message: 'Ticket closed' };

    service.closeTicket(ticketId).subscribe(response => {
      expect(response).toEqual(mockPatchResponse);
    });

    const req = httpTestingController.expectOne(`${environment.apiUrl}/tickets/${ticketId}/close`);
    expect(req.request.method).toBe('PATCH');
    req.flush(mockPatchResponse);
  });
});
