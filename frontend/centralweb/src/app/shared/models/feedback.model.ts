export class TicketFeedback {
    constructor(
      public id: number,
      public ticketId: number,
      public userId: number,
      public rating: number,
      public comment: string,
      public createdAt: Date
    ) {}
  }
  
  export interface CreateTicketFeedback {
    ticketId: number;
    userId: number;
    rating: number;
    comment: string;
  }
  