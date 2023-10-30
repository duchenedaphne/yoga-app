
/*********  LOGIN  ************/
describe('Login spec', () => {
  it('Login and logout successfull', () => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'yoga@studio.com',
        firstName: 'Admin',
        lastName: 'Admin',
        admin: true
      },
    });

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session');

    cy.get('input[formControlName=email]').type("yoga@studio.com");
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);

    cy.url().should('include', '/sessions');

    // it 'logout sucessfull'
    cy.get('[data-testid="logout-link"]').click();

    cy.contains('span', 'Register').should('be.visible');
  });

  it('return an error message with wrong credentials', () => {
    cy.visit('/login');
    
    cy.get('input[formControlName=email]').type("wrong@email.com");
    cy.get('input[formControlName=password]').type(`${"wrong-password"}{enter}{enter}`);
    
    cy.get('[data-testid="error-message"]').should('be.visible');
  });
});

/*********  HOME AND NOT FOUND  ************/
describe('Home and Not found pages', () => {
  it('Visits the app', () => {
    cy.visit('/');

    cy.contains('span', 'Yoga app').should('be.visible');
  });

  it('redirect to the not found page', () => {
    cy.visit('/wrong-url');

    cy.url().should('include', '/404');
  });
});

/*********  REGISTER  ************/
describe('Register spec', () => {
  it('Register successfull', () => {
    cy.visit('/register');

    cy.intercept('POST', '/api/auth/register', {
      statusCode: 200
    });

    cy.get('input[formControlName=firstName]').type('Test');
    cy.get('input[formControlName=lastName]').type('Test');
    cy.get('input[formControlName=email]').type('test@email.com');
    cy.get('input[formControlName=password]').type(`${'password'}{enter}{enter}`);

    cy.url().should('include', '/login');
  });
});

/*********  ME  ************/
describe('User spec', () => {

  it('show the user detail', () => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 2,
        username: 'user@test.com',
        firstName: 'User',
        lastName: 'Test',
        admin: false
      },
    });

    cy.intercept('GET', '/api/user/2', {
      id: 2,
      email: 'user@test.com',
      lastName: 'Test',
      firstName: 'User',
      admin: false,
      createdAt: '2023-10-30',
      updatedAt: '2023-10-30',
    });

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session');

    cy.get('input[formControlName=email]').type("user@test.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);

    cy.url().should('include', '/sessions');

    cy.contains('span', 'Account').should('be.visible');

    cy.get('[data-testid="me-link"]').click();
    
    cy.url().should('include', '/me');

    cy.contains('h1', 'User information').should('be.visible');

    // it 'return to the sessions page' 
    cy.get('[data-testid="back-button"]').click();

    cy.url().should('include', '/sessions');
    
    // it 'delete the user'
    cy.get('[data-testid="me-link"]').click();

    cy.intercept('DELETE', '/api/user/2', {
      statuscode: 200
    });

    cy.get('[data-testid="delete-button"]').click();

    cy.contains('span', 'Login').should('be.visible');
  })
});

/*********  SESSIONS  ************/
describe('Session spec - Admin', () => {
  it('show the sessions list and manage the sessions', () => {
    cy.visit('/login')

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session');

    cy.intercept('POST', '/api/session', {
      body: [
        {
          id: 1,
          name: 'Beginners',
          description: 'Beginners session test',
          date: '2023-11-30T00:00:00.000+00:00',
          teacher_id: 2,
          users: []
        }
      ]
    });

    cy.intercept('GET', '/api/session', {
      body: [
        {
          id: 1,
          name: 'Beginners',
          description: 'Beginners session test',
          date: '2023-11-30T00:00:00.000+00:00',
          teacher_id: 2,
          users: []
        }
      ]
    });

    cy.intercept('POST', '/api/session/1', {
      body: {
        id: 1,
        name: 'Beginners',
        description: 'Beginners session test',
        date: '2023-11-30T00:00:00.000+00:00',
        teacher_id: 2,
        users: []
      }
    });

    cy.intercept('GET', '/api/session/1', {
        id: 1,
        name: 'Beginners',
        description: 'Beginners session test',
        date: '2023-11-30T00:00:00.000+00:00',
        teacher_id: 2,
        users: []
    });
  
    cy.intercept('POST', '/api/teacher', { 
      body: [
        { id: 1, firstName: 'Margot', lastName: 'DELAHAYE' },
        { id: 2, firstName: 'Hélène', lastName: 'THIERCELIN' }
      ] 
    });
  
    cy.intercept('GET', '/api/teacher', { 
      body: [
        { id: 1, firstName: 'Margot', lastName: 'DELAHAYE' },
        { id: 2, firstName: 'Hélène', lastName: 'THIERCELIN' }
      ] 
    });

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'yoga@studio.com',
        firstName: 'Admin',
        lastName: 'Admin',
        admin: true
      },
    })

    cy.intercept('GET', '/api/user/1', {
      id: 1,
      email: 'yoga@studio.com',
      lastName: 'Admin',
      firstName: 'Admin',
      admin: true,
      createdAt: '2023-10-30',
      updatedAt: '2023-10-30',
    });

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    // it 'show sessions list'
    cy.url().should('include', '/sessions');

    cy.contains('mat-card-title', 'Rentals available').should('be.visible');
    
    // it 'create a session'
    cy.get('[data-testid="create-session-btn"]').click();

    cy.url().should('include', '/sessions/create');

    cy.get('input[formControlName=name]').type("yoga");
    cy.get('input[formControlName=date]').type("2023-11-30");
    cy.get('mat-select[formControlName=teacher_id]').click().get('mat-option').contains('Hélène THIERCELIN').click();
    cy.get('textarea[formControlName=description]').type("nouvelle session test");

    cy.get('button[type="submit"]').click();

    cy.url().should('include', '/sessions');

    // it 'show a session detail' 
    cy.get('[data-testid="detail-session-btn"]').click();

    cy.url().should('include', '/sessions/detail/1')

    cy.contains('span', 'Delete').should('be.visible');
  
    // it 'delete a session'
    cy.intercept('DELETE', '/api/session/1', {
      statuscode: 200
    })
    cy.get('[data-testid="delete-session-btn"]').click();

    cy.url().should('include', '/sessions')

  // it 'update a session'
    cy.get('[data-testid="update-session-btn"]').click();

    cy.url().should('include', '/sessions/update/1');

    cy.get('input[formControlName=name]').type("yoga");
    cy.get('input[formControlName=date]').type("2023-11-30");
    cy.get('mat-select[formControlName=teacher_id]').click().get('mat-option').contains('Hélène THIERCELIN').click();
    cy.get('textarea[formControlName=description]').clear();
    cy.get('textarea[formControlName=description]').type("update session test");

    cy.get('button[type="submit"]').click();

    cy.url().should('include', '/sessions')
  });
});




