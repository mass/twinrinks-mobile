//
//  SettingsViewController.m
//  TwinRinksAdultHockey
//
//  Created by Andrew on 1/7/13.
//  Copyright (c) 2013 GigaStorm. All rights reserved.
//

#import "SettingsViewController.h"

@interface SettingsViewController ()

@end

@implementation SettingsViewController

- (IBAction)switch_autologin:(id)sender {
    BOOL isOn = ((UISwitch*) sender).on;
    
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    [defaults setBool:isOn forKey:@"IsAutoLoginOn"];
    [defaults synchronize];
    
    if(!isOn) {
        self.autoLoginLabel.enabled = NO;
        self.autoLoginButton.enabled = NO;
    }
    else {
        self.autoLoginButton.enabled = YES;
        self.autoLoginLabel.enabled = YES;
    }
}

- (IBAction)btn_autologin:(id)sender {
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Configure Automatic Login" message:nil delegate:self cancelButtonTitle:@"Save" otherButtonTitles:nil, nil];
    alert.alertViewStyle = UIAlertViewStyleLoginAndPasswordInput;
    
    UITextField *alertUserNameTF = [alert textFieldAtIndex:0];
    alertUserNameTF.keyboardType = UIKeyboardTypeEmailAddress;
    alertUserNameTF.placeholder = @"Enter Login Email Address";
    
    UITextField *alertPasswordTF = [alert textFieldAtIndex:1];
    alertPasswordTF.placeholder = @"Enter Login Password";
    
    [alert show];
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    
    [defaults setObject:[[alertView textFieldAtIndex:0] text] forKey:@"SavedUsername"];
    [defaults setObject:[[alertView textFieldAtIndex:1] text] forKey:@"SavedPassword"];
    [defaults synchronize];
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    BOOL isAutoLoginOn = [defaults boolForKey:@"IsAutoLoginOn"];
    self.autoLoginSwitch.on = isAutoLoginOn;
    
    if(!isAutoLoginOn) {
        self.autoLoginLabel.enabled = NO;
        self.autoLoginButton.enabled = NO;
    }
    else {
        self.autoLoginButton.enabled = YES;
        self.autoLoginLabel.enabled = YES;
    }
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
