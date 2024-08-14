import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { TicketService } from '../../shared/services/ticket.service';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { CommonModule, DatePipe } from '@angular/common';
import { FeedbackService } from '../../shared/services/feedback.service';
import { TicketFeedback } from '../../shared/models/feedback.model';

@Component({
  selector: 'app-ticket-create',
  templateUrl: './ticket-create.component.html',
  styleUrls: ['./ticket-create.component.css'],
  standalone: true,
  imports: [
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    ReactiveFormsModule,
    MatIconModule,
    MatSnackBarModule,
    CommonModule,
  ],
  providers: [DatePipe]
})
export class TicketCreateComponent implements OnInit {
  ticketForm: FormGroup;
  public feedbackReceived: TicketFeedback | null = null;

  constructor(
    private fb: FormBuilder,
    private ticketService: TicketService,
    public dialogRef: MatDialogRef<TicketCreateComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private snackBar: MatSnackBar,
    private feedbackService: FeedbackService,
  ) {
    this.ticketForm = this.fb.group({
      subject: ['', Validators.required],
      description: ['', Validators.required],
    });

    if (this.data.id) {
      this.ticketForm.patchValue(this.data);
      this.ticketForm.disable();
    }
  }

  ngOnInit(): void {
    if (this.data.hasFeedback && this.data.id) {
      this.feedbackService.getFeedback(this.data.id).subscribe({
        next: (feedback: TicketFeedback) => {
          this.feedbackReceived = feedback;
        },
        error: (err) => {
          console.error('Error retrieving feedback', err);
          this.feedbackReceived = null;
        }
      });
    }
  }


  saveTicket(): void {
    if (this.ticketForm.valid) {
      const newTicket = { ...this.ticketForm.value, userId: this.data.userId };
      this.ticketService.createTicket(newTicket).subscribe({
        next: (ticket) => {
          this.snackBar.open('Ticket saved successfully', 'Close', { duration: 3000 });
          this.dialogRef.close(ticket);
        },
        error: (error) => {
          console.error('There was an error!', error);
          this.snackBar.open('Error saving ticket', 'Close', { duration: 3000 });
        }
      });
    }
  }
}

