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
    
    UIColor *lightGrey =[UIColor colorWithRed:.66f green:.66f blue:.66f alpha:1.0f];
    UIColor *defBlue = [UIColor colorWithRed:.196f green:.309f blue:.521f alpha:1.00f];
    
    if(!isOn) {
        self.autoLoginLabel.enabled = NO;
        self.autoLoginButton.enabled = NO;
        self.autoLoginButton.titleLabel.textColor = lightGrey;
    }
    else {
        self.autoLoginButton.enabled = YES;
        self.autoLoginLabel.enabled = YES;
        self.autoLoginButton.titleLabel.textColor = defBlue;
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

//- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
//{
//    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
//    if (self) {
//        // Custom initialization
//    }
//    return self;
//}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    BOOL isAutoLoginOn = [defaults boolForKey:@"IsAutoLoginOn"];
    self.autoLoginSwitch.on = isAutoLoginOn;
    
    UIColor *lightGrey =[UIColor colorWithRed:.66f green:.66f blue:.66f alpha:1.0f];
    UIColor *defBlue = [UIColor colorWithRed:.196f green:.309f blue:.521f alpha:1.00f];
    
    if(!isAutoLoginOn) {
        self.autoLoginLabel.enabled = NO;
        self.autoLoginButton.enabled = NO;
        self.autoLoginButton.titleLabel.textColor = lightGrey;
    }
    else {
        self.autoLoginButton.enabled = YES;
        self.autoLoginLabel.enabled = YES;
        self.autoLoginButton.titleLabel.textColor = defBlue;
    }
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
